namespace MyStoreBack.Business_logic.Files;

public interface IPictureService
{
    Task<string> Save(IFormFile? file);
    // Task<string> Save(string base64);

    void RemoveByUrl(string url);
}