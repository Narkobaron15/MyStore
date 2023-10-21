using System.ComponentModel.DataAnnotations;

namespace MyStoreBack.Models;

public class RegisterModel
{
    public string Email { get; set; } = String.Empty;
    public string Password { get; set; } = String.Empty;
    [StringLength(100), Required]
    public string FirstName { get; set; } = String.Empty;
    [StringLength(100), Required]
    public string LastName { get; set; } = String.Empty;
    [StringLength(300)]
    public string? Image { get; set; }
}