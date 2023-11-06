using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using MyStoreBack.Business_logic.Category;
using MyStoreBack.Models.Category;

namespace MyStoreBack.Controllers;

[ApiController, Authorize, Route("[controller]")]
public class CategoriesController : ControllerBase
{
    private ICategoryService Service { get; }
    public CategoriesController(ICategoryService service)
    {
        Service = service;
    }
    
    [HttpGet]
    public async Task<IActionResult> Get()
        // It would be great to make ranged response here
    {
        var cats = await Service.GetAll();
        return Ok(cats);
    }

    [HttpGet("{id:int}")]
    public async Task<IActionResult> GetById([FromRoute] int id)
    {
        CategoryModel cat = await Service.GetById(id);
        return Ok(cat);
    }
    
    [HttpPost("create")]
    public async Task<IActionResult> Create([FromForm] CategoryCreateModel model)
    {
        CategoryModel? cat = await Service.Create(model);
        return cat is not null
            ? Ok(cat)
            : BadRequest(new { message = "Failed to create category." });
    }
    
    [HttpPut("update")]
    public async Task<IActionResult> Update([FromForm] CategoryUpdateModel model)
    {
        CategoryModel? updated = await Service.Update(model);
        return updated is not null
            ? Ok(updated)
            : BadRequest(new { message = "Failed to update category." });
    }
    
    [HttpDelete("delete/{id:int}")]
    public async Task<IActionResult> Delete([FromRoute] int id)
    {
        bool result = await Service.Delete(id);
        return result ? Ok() : BadRequest(new { message = "Failed to delete category."});
    }
}
