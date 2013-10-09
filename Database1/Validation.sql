CREATE TABLE [dbo].[Validation]
(
	[Id] INT NOT NULL PRIMARY KEY, 
    [tipo] INT NOT NULL, 
    [stamp] TIMESTAMP NOT NULL, 
    [user] INT NOT NULL, 
	[spot] INT NOT NULL, 
    CONSTRAINT [FK_user_id] FOREIGN KEY([user] ) REFERENCES [dbo].[User]([Id]),
	CONSTRAINT [FK_spot_id] FOREIGN KEY([spot] ) REFERENCES [dbo].[Spot]([Id])

)
