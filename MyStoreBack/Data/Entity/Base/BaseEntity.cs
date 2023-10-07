namespace MyStoreBack.Data.Entity.Base;

public class BaseEntity<T>: IEntity<T>
{
    public T Id { get; set; }
    public bool IsDeleted { get; set; } = false;
    public DateTime CreatedAt { get; set; }
}