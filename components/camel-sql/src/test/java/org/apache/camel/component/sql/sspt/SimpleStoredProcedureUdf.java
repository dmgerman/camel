begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.sql.sspt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|sspt
package|;
end_package

begin_comment
comment|/**  * Created by snurmine on 12/20/15.  */
end_comment

begin_class
DECL|class|SimpleStoredProcedureUdf
specifier|public
class|class
name|SimpleStoredProcedureUdf
block|{
DECL|method|addnumbers (int VALUE1, int VALUE2, int[] RESULT)
specifier|public
specifier|static
name|void
name|addnumbers
parameter_list|(
name|int
name|VALUE1
parameter_list|,
name|int
name|VALUE2
parameter_list|,
name|int
index|[]
name|RESULT
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"calling addnumbers:"
operator|+
name|VALUE1
operator|+
literal|","
operator|+
name|VALUE2
argument_list|)
expr_stmt|;
name|RESULT
index|[
literal|0
index|]
operator|=
name|VALUE1
operator|+
name|VALUE2
expr_stmt|;
block|}
block|}
end_class

end_unit

