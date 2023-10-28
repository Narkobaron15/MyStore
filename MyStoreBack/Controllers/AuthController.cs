using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using MyStoreBack.Business_logic.Authentication;
using MyStoreBack.Models.Identity;

namespace MyStoreBack.Controllers;

[ApiController]
[Route("[controller]")]
public class AuthController : ControllerBase
{
    private IAuthService AuthService { get; }
    public AuthController(IAuthService authService)
    {
        AuthService = authService;
    }

    [AllowAnonymous]
    [HttpPost("login")]
    public async Task<IActionResult> Login([FromBody] LoginModel model)
    {
        try
        {
            var tokens = await AuthService.Login(model);
            return Ok(tokens);
        }
        catch (Exception ex)
        {
            return Unauthorized(new { message = ex.Message });
        }
    }

    [AllowAnonymous]
    [HttpPost("register")]
    public async Task<IActionResult> Register([FromBody] RegisterModel model)
    {
        try
        {
            await AuthService.Register(model);
            return Ok();
        }
        catch (Exception ex)
        {
           return BadRequest(new { message = ex.Message });
        }
    }
    
    [HttpPost("refresh")]
    public async Task<IActionResult> Refresh()
    {
        await Task.Delay(1000);
        return NotFound();
    }
}