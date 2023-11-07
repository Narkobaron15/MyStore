namespace MyStoreBack.Business_logic.Files;

public class LocalPictureService : IPictureService
{
    private const string ApiUrl = "https://narkobaron.ninja/uploads/";
    private const string Ext = ".webp";
    
    // https://github.com/SixLabors/ImageSharp
    public async Task<string> Save(IFormFile? file)
    {
        if (file is null)
            return ApiUrl + "default.png";
        
        // Resize image
        await using var stream = file.OpenReadStream();
        using var image = await Image.LoadAsync(stream);
        
        var resizeOpts = new ResizeOptions
        {
            Size = image.Size,
            Mode = ResizeMode.Max
        };
        image.Mutate(cnt => cnt.Resize(resizeOpts));

        string imageName = Path.GetRandomFileName()[..^4] + Ext,
            dirSaveImage = ImageInUploads(imageName);

        // await using uses IAsyncDisposable instead of IDisposable
        await using var fileStream = File.Create(dirSaveImage);
        await image.SaveAsWebpAsync(fileStream);

        return ApiUrl + imageName;
    }

    public void RemoveByUrl(string url)
    {
        string imageName = url[ApiUrl.Length..],
            pathToFile = ImageInUploads(imageName);
        
        if (pathToFile != ImageInUploads("default.png"))
            File.Delete(pathToFile);
    }

    private static string ImageInUploads(string imgName)
        => Path.Combine(
            Directory.GetCurrentDirectory(),
            "Uploads", imgName
        );
}