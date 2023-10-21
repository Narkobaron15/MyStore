using MyStoreBack.Models;
using MyStoreBack.Models.Category;

namespace MyStoreBack.Business_logic.Category;

public interface ICategoryService
{
    public Task<IEnumerable<CategoryModel>> GetAll();
    public Task<CategoryModel> GetById(int id);
    public Task<CategoryModel?> Create(CategoryCreateModel model);
    public Task<CategoryModel?> Update(CategoryUpdateModel model);
    public Task<bool> Delete(int id);
}