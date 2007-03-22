begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *   */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|FluentArg
import|;
end_import

begin_class
DECL|class|MethodInfo
specifier|public
class|class
name|MethodInfo
block|{
DECL|field|method
specifier|final
name|Method
name|method
decl_stmt|;
DECL|field|parameters
specifier|final
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
name|parameters
decl_stmt|;
DECL|field|annotations
specifier|final
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|FluentArg
argument_list|>
name|annotations
decl_stmt|;
DECL|method|MethodInfo (Method method, LinkedHashMap<String, Class> parameters, LinkedHashMap<String, FluentArg> annotations)
specifier|public
name|MethodInfo
parameter_list|(
name|Method
name|method
parameter_list|,
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
name|parameters
parameter_list|,
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|FluentArg
argument_list|>
name|annotations
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
name|this
operator|.
name|annotations
operator|=
name|annotations
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|method
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

