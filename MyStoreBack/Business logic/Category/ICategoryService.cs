using System.Security.Claims;
using MyStoreBack.Models;
using MyStoreBack.Models.Category;

namespace MyStoreBack.Business_logic.Category;

public interface ICategoryService
{
    public Task<IEnumerable<CategoryModel>> GetAll(ClaimsPrincipal user);
    public Task<CategoryModel> GetById(int id, ClaimsPrincipal user);
    public Task<CategoryModel?> Create(CategoryCreateModel model, ClaimsPrincipal user);
    public Task<CategoryModel?> Update(CategoryUpdateModel model, ClaimsPrincipal user);
    public Task<bool> Delete(int id, ClaimsPrincipal user);
}