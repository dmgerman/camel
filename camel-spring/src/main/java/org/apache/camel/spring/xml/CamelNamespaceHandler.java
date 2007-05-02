begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.spring.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|CamelContextFactoryBean
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
name|spring
operator|.
name|EndpointFactoryBean
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
name|xml
operator|.
name|NamespaceHandlerSupport
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
name|xml
operator|.
name|ParserContext
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
name|support
operator|.
name|BeanDefinitionBuilder
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
name|config
operator|.
name|BeanDefinition
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
name|parsing
operator|.
name|BeanComponentDefinition
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
name|PropertyValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_class
DECL|class|CamelNamespaceHandler
specifier|public
class|class
name|CamelNamespaceHandler
extends|extends
name|NamespaceHandlerSupport
block|{
DECL|field|routesParser
specifier|protected
name|CamelBeanDefinitionParser
name|routesParser
init|=
operator|new
name|CamelBeanDefinitionParser
argument_list|()
decl_stmt|;
DECL|field|endpointParser
specifier|protected
name|BeanDefinitionParserSupport
name|endpointParser
init|=
operator|new
name|BeanDefinitionParserSupport
argument_list|(
name|EndpointFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|registerBeanDefinitionParser
argument_list|(
literal|"routes"
argument_list|,
name|routesParser
argument_list|)
expr_stmt|;
name|registerBeanDefinitionParser
argument_list|(
literal|"routeBuilder"
argument_list|,
name|routesParser
argument_list|)
expr_stmt|;
name|registerBeanDefinitionParser
argument_list|(
literal|"endpoint"
argument_list|,
name|endpointParser
argument_list|)
expr_stmt|;
name|registerBeanDefinitionParser
argument_list|(
literal|"camelContext"
argument_list|,
operator|new
name|BeanDefinitionParserSupport
argument_list|(
name|CamelContextFactoryBean
operator|.
name|class
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|doParse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|parserContext
parameter_list|,
name|BeanDefinitionBuilder
name|builder
parameter_list|)
block|{
name|super
operator|.
name|doParse
argument_list|(
name|element
argument_list|,
name|parserContext
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|NodeList
name|list
init|=
name|element
operator|.
name|getElementsByTagName
argument_list|(
literal|"routes"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|size
init|=
name|list
operator|.
name|getLength
argument_list|()
init|,
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|node
init|=
operator|(
name|Element
operator|)
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|BeanDefinition
name|definition
init|=
name|routesParser
operator|.
name|parseInternal
argument_list|(
name|node
argument_list|,
name|parserContext
argument_list|)
decl_stmt|;
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"routeBuilder"
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
name|list
operator|=
name|element
operator|.
name|getElementsByTagName
argument_list|(
literal|"endpoint"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|size
init|=
name|list
operator|.
name|getLength
argument_list|()
init|,
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|node
init|=
operator|(
name|Element
operator|)
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|BeanDefinition
name|definition
init|=
name|endpointParser
operator|.
name|parse
argument_list|(
name|node
argument_list|,
name|parserContext
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|node
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|addPropertyValue
argument_list|(
literal|"context"
argument_list|,
name|builder
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|parserContext
operator|.
name|registerComponent
argument_list|(
operator|new
name|BeanComponentDefinition
argument_list|(
name|definition
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

