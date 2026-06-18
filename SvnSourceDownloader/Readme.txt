### Database based Source Downloader 

create table SourceDownload(
Sno int identity,
[Platform] varchar(20),
[FromURL] nvarchar(max),
[ToURL] nvarchar(max),
[Username] varchar(20),
[Password] varchar(15),
[BackUpTime] int
)


select * from SourceDownload


--insert into SourceDownload(Platform,FromURL,ToURL,Username,Password,BackUpTime) values
('SVN','','','Venkatesh','jil@123',8 )
