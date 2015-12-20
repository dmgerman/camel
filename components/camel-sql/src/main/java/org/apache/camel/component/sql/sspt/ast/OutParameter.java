begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.sql.sspt.ast
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
operator|.
name|ast
package|;
end_package

begin_comment
comment|/**  * Created by snurmine on 12/20/15.  */
end_comment

begin_class
DECL|class|OutParameter
specifier|public
class|class
name|OutParameter
block|{
DECL|field|name
name|String
name|name
decl_stmt|;
DECL|field|sqlType
name|int
name|sqlType
decl_stmt|;
DECL|field|outHeader
name|String
name|outHeader
decl_stmt|;
DECL|method|OutParameter (String name, int sqlType, String outHeader)
specifier|public
name|OutParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|sqlType
parameter_list|,
name|String
name|outHeader
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|sqlType
operator|=
name|sqlType
expr_stmt|;
name|this
operator|.
name|outHeader
operator|=
name|outHeader
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getSqlType ()
specifier|public
name|int
name|getSqlType
parameter_list|()
block|{
return|return
name|sqlType
return|;
block|}
DECL|method|getOutHeader ()
specifier|public
name|String
name|getOutHeader
parameter_list|()
block|{
return|return
name|outHeader
return|;
block|}
block|}
end_class

end_unit

