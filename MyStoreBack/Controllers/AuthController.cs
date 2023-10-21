using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using MyStoreBack.Data.Entity.Identity;
using MyStoreBack.Models;
using MyStoreBack.Security;

namespace MyStoreBack.Controllers;

[ApiController]
[Route("/api/[controller]")]
public class AuthController : ControllerBase
{
    private readonly IJwtTokenService _jwtTokenService;
    private readonly UserManager<UserEntity> _userManager;

    public AuthController(
        IJwtTokenService jwtTokenService, 
        UserManager<UserEntity> userManager)
    {
        _jwtTokenService = jwtTokenService;
        _userManager = userManager;
    }

    [HttpPost("login")]
    public async Task<IActionResult> Login([FromBody] LoginModel model)
    {
        UserEntity user = await _userManager.FindByEmailAsync(model.Email);
        if (user is null)
            return BadRequest("Invalid credentials.");

        bool isPasswordValid = await _userManager.CheckPasswordAsync(user, model.Password);
        if (!isPasswordValid)
            return BadRequest("Invalid credentials.");

        string token = await _jwtTokenService.CreateTokenAsync(user);
        return Ok(new { token });
    }

    [HttpPost("register")]
    public async Task<IActionResult> Register([FromBody] RegisterModel model)
    {
        await Task.Delay(1000);
        return NotFound();
    }
    
    [HttpPost("refresh")]
    public async Task<IActionResult> Refresh()
    {
        await Task.Delay(1000);
        return NotFound();
    }
}