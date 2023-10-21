namespace MyStoreBack.Models.Identity;

public class Tokens
{
    public string AccessToken { get; set; } = String.Empty;
    public string RefreshToken { get; set; } = String.Empty;
}