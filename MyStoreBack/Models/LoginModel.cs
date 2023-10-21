namespace MyStoreBack.Models;

/// <summary>
/// The model that holds the user data on login.
/// </summary>
public class LoginModel
{
    /// <summary>
    /// Email of the user.
    /// </summary>
    /// <example>example@example.com</example>
    public string Email { get; set; } = String.Empty;
    /// <summary>
    /// Password of the user.
    /// </summary>
    public string Password { get; set; } = String.Empty;
    
    /// <summary>
    /// The flag that indicates whether the user wants to be remembered.
    /// </summary>
    public bool RememberMe { get; set; }
}