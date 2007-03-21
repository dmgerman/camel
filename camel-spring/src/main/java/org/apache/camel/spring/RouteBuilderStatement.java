begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|BeanFactory
import|;
end_import

begin_class
DECL|class|RouteBuilderStatement
specifier|public
class|class
name|RouteBuilderStatement
block|{
DECL|field|actions
specifier|private
name|ArrayList
argument_list|<
name|RouteBuilderAction
argument_list|>
name|actions
decl_stmt|;
DECL|method|create (BeanFactory beanFactory, Object builder)
specifier|public
name|void
name|create
parameter_list|(
name|BeanFactory
name|beanFactory
parameter_list|,
name|Object
name|builder
parameter_list|)
block|{
name|Object
name|currentBuilder
init|=
name|builder
decl_stmt|;
name|RouteBuilderAction
name|lastAction
init|=
literal|null
decl_stmt|;
for|for
control|(
name|RouteBuilderAction
name|action
range|:
name|actions
control|)
block|{
comment|// The last action may have left us without a builder to invoke next!
if|if
condition|(
name|builder
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid route configuration.  The '"
operator|+
name|lastAction
operator|.
name|getName
argument_list|()
operator|+
literal|"' action cannot be followed by the '"
operator|+
name|action
operator|.
name|getName
argument_list|()
operator|+
literal|"' action."
argument_list|)
throw|;
block|}
name|currentBuilder
operator|=
name|action
operator|.
name|invoke
argument_list|(
name|beanFactory
argument_list|,
name|currentBuilder
argument_list|)
expr_stmt|;
name|lastAction
operator|=
name|action
expr_stmt|;
block|}
block|}
DECL|method|getActions ()
specifier|public
name|ArrayList
argument_list|<
name|RouteBuilderAction
argument_list|>
name|getActions
parameter_list|()
block|{
return|return
name|actions
return|;
block|}
DECL|method|setActions (ArrayList<RouteBuilderAction> actions)
specifier|public
name|void
name|setActions
parameter_list|(
name|ArrayList
argument_list|<
name|RouteBuilderAction
argument_list|>
name|actions
parameter_list|)
block|{
name|this
operator|.
name|actions
operator|=
name|actions
expr_stmt|;
block|}
block|}
end_class

end_unit

