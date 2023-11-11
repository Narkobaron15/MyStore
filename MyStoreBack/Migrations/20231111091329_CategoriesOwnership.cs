using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace MyStoreBack.Migrations
{
    public partial class CategoriesOwnership : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "UserId",
                table: "tbl_categories",
                type: "integer",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_categories_UserId",
                table: "tbl_categories",
                column: "UserId");

            migrationBuilder.AddForeignKey(
                name: "FK_tbl_categories_AspNetUsers_UserId",
                table: "tbl_categories",
                column: "UserId",
                principalTable: "AspNetUsers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_tbl_categories_AspNetUsers_UserId",
                table: "tbl_categories");

            migrationBuilder.DropIndex(
                name: "IX_tbl_categories_UserId",
                table: "tbl_categories");

            migrationBuilder.DropColumn(
                name: "UserId",
                table: "tbl_categories");
        }
    }
}
