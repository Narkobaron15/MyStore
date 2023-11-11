namespace MyStoreBack.Business_logic.Files;

public class LocalPictureService : IPictureService
{
    private const string ApiUrl = "https://narkobaron.ninja/uploads/";
    private const string WebpExt = ".webp";

    // https://github.com/SixLabors/ImageSharp
    private static async Task<string> Save(Stream stream)
    {
        using var image = await Image.LoadAsync(stream);
        
        var resizeOpts = new ResizeOptions
        {
            Size = image.Size,
            Mode = ResizeMode.Max
        };
        image.Mutate(cnt => cnt.Resize(resizeOpts));

        string imageName = Path.GetRandomFileName()[..^4] + WebpExt,
            dirSaveImage = ImageInUploads(imageName);

        // await using uses IAsyncDisposable instead of IDisposable
        await using var fileStream = File.Create(dirSaveImage);
        await image.SaveAsWebpAsync(fileStream);

        return ApiUrl + imageName;
    }
    
    public async Task<string> Save(IFormFile? file)
    {
        if (file is null)
            return ApiUrl + "default.png";
        
        // Resize image
        await using var stream = file.OpenReadStream();
        return await Save(stream);
    }

    public async Task<string> Save(Uri uri)
    {
        using HttpClient client = new();
        var stream = await client.GetStreamAsync(uri);
        return await Save(stream);
    }

    public void RemoveByUrl(string url)
    {
        if (!url.StartsWith(ApiUrl))
            return;
        
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