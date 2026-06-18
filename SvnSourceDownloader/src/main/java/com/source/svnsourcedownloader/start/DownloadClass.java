package com.source.svnsourcedownloader.start;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.jora.encodedecode.common.EncryptionDecryption;
import com.source.svnsourcedownloader.connection.ConnectionConfig;
import com.source.svnsourcedownloader.service.SourceDownloadService;

@Component
@EnableScheduling
public class DownloadClass {

	private String svnPath, destinationPath, userName, password, currentDate;
	private File localDir;

	@Scheduled(cron = "${cron.backuptime}")
	public void process() throws Exception {
		getSvnDetails();
		sourceDownload();

		if (EncryptionDecryption.decrypt(ConnectionConfig.getOtherSourceBakNeed()).equalsIgnoreCase("Y")) {
			otherSourceDownload();
		}
		zipSourceFolder();

	}

	private void zipSourceFolder() throws Exception {

		/*** ZIP Folder ***/
		zipFolder(destinationPath.concat("\\").concat(currentDate),
				destinationPath.concat("\\Date\\").concat(currentDate).concat(".zip"));

		System.out.println("Source Zipped Successfully!...");
	}

	private void otherSourceDownload() throws Exception {
		Path fromPath = Paths.get(EncryptionDecryption.decrypt(ConnectionConfig.getOtherSourcePath()));
		Path toPath = Paths.get(destinationPath.concat("\\").concat(currentDate).concat("\\OtherSource"));

		Files.walk(fromPath).forEach(path -> {
			try {
				Path targetPath = toPath.resolve(fromPath.relativize(path));
				if (Files.isDirectory(path)) {
					System.out.println(path);
					Files.createDirectories(targetPath);
				} else {
					Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		System.out.println("Other Source Downloaded Successfully!...");

	}

	private void getSvnDetails() throws Exception {

		if (ConnectionConfig.getBasedOn().equalsIgnoreCase("D")) {

			SourceDownloadService sourceDownloadService = SvnSourceDownloaderApplication.applicationContext
					.getBean(SourceDownloadService.class);
			Map<String, Object> sourceDownload = sourceDownloadService.getSourceDownloadDetails();
			svnPath = String.valueOf(sourceDownload.get("FromURL"));
			destinationPath = String.valueOf(sourceDownload.get("ToURL"));
			userName = String.valueOf(sourceDownload.get("Username"));
			password = String.valueOf(sourceDownload.get("Password"));

		} else {
			svnPath = EncryptionDecryption.decrypt(ConnectionConfig.getSvnUrl());
			destinationPath = EncryptionDecryption.decrypt(ConnectionConfig.getDestinationUrl());
			userName = EncryptionDecryption.decrypt(ConnectionConfig.getSvnUserid());
			password = EncryptionDecryption.decrypt(ConnectionConfig.getSvnPassword());

		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY");
		currentDate = dateFormat.format(new Date());
		localDir = new File(destinationPath.concat("\\").concat(currentDate).concat("\\SVN"));

	}

	@SuppressWarnings("deprecation")
	private void sourceDownload() throws Exception {
		try {

			folderCreation();

			/** SVN LOGIN **/
			ISVNAuthenticationManager authManager = new BasicAuthenticationManager(userName, password);
			SVNClientManager clientManager = SVNClientManager.newInstance(SVNWCUtil.createDefaultOptions(true),
					authManager);

			/** SVN URL **/
			SVNURL repositoryURL = SVNURL.parseURIEncoded(svnPath);

			/*** For Check out ***/
			SVNUpdateClient updateClient = clientManager.getUpdateClient();

			/*** Save Process ***/
			long revision = updateClient.doCheckout(repositoryURL, localDir, SVNRevision.HEAD, SVNRevision.HEAD, true);

			System.out.println("Source Downloaded Successfully!...\n" + "Checked out revision " + revision);

		} catch (Exception e) {
			throw e;
		}
	}

	private void folderCreation() throws Exception {

		deleteAndCreateFolder(destinationPath.concat("\\").concat(currentDate));
		deleteAndCreateFolder(destinationPath.concat("\\").concat(currentDate).concat("\\SVN"));

		/*** Other Source Backup Folder ***/
		if (EncryptionDecryption.decrypt(ConnectionConfig.getOtherSourceBakNeed()).equalsIgnoreCase("Y")) {
			deleteAndCreateFolder(destinationPath.concat("\\").concat(currentDate).concat("\\OtherSource"));
		}

		Path zipFolderPath = Paths.get(destinationPath.concat("\\").concat("Date"));

		if (!Files.exists(zipFolderPath)) {
			Files.createDirectory(zipFolderPath);
		}

	}

	private void deleteAndCreateFolder(String toFolder) throws Exception {
		Path folderPath = Paths.get(toFolder);

		/*** Check if the folder exists ***/
		if (Files.exists(folderPath)) {
			Files.setAttribute(folderPath, "dos:readonly", false);
			deleteFolder(folderPath);
		}

		/*** Create a new folder ***/
		Files.createDirectory(folderPath);

	}

	public static void deleteFolder(Path folderPath) throws IOException {

		Files.walkFileTree(folderPath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	@SuppressWarnings("resource")
	private void zipFolder(String folderPath, String zipPath) throws IOException {
		FileOutputStream fos = new FileOutputStream(zipPath);
		ZipOutputStream zos = new ZipOutputStream(fos);

		File sourceFolder = new File(folderPath);
		if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
			throw new IllegalArgumentException("Invalid folder: " + folderPath);
		}

		zipFile(sourceFolder, sourceFolder.getName(), zos);

		zos.close();
		fos.close();
	}

	private void zipFile(File sourceFolder, String fileName, ZipOutputStream zos) throws IOException {
		if (sourceFolder.isHidden()) {
			return;
		}

		if (sourceFolder.isDirectory()) {
			if (fileName.endsWith("/")) {
				zos.putNextEntry(new ZipEntry(fileName));
				zos.closeEntry();
			} else {
				zos.putNextEntry(new ZipEntry(fileName + "/"));
				zos.closeEntry();
			}

			File[] children = sourceFolder.listFiles();
			if (children != null) {
				for (File childFile : children) {
					zipFile(childFile, fileName + "/" + childFile.getName(), zos);
				}
			}
			return;
		}

		FileInputStream fis = new FileInputStream(sourceFolder);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer)) >= 0) {
			zos.write(buffer, 0, length);
		}

		fis.close();
		zos.closeEntry();

	}

}
