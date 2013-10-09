CREATE TABLE [dbo].[User]
(
	[Id] INT NOT NULL PRIMARY KEY, 
    [name] NCHAR(10) NULL, 
    [password] NCHAR(10) NULL, 
    [email] NCHAR(10) NULL, 
    [t1] INT NULL, 
    [t2] INT NULL, 
    [t3] INT NULL
)
