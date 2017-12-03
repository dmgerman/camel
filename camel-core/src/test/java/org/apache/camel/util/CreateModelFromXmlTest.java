begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|ContextTestSupport
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
name|Expression
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
name|model
operator|.
name|ExpressionNode
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
name|model
operator|.
name|ModelHelper
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
name|model
operator|.
name|RouteDefinition
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
name|model
operator|.
name|RoutesDefinition
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
name|model
operator|.
name|language
operator|.
name|ExpressionDefinition
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
name|spi
operator|.
name|NamespaceAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|ProcessorDefinitionHelper
operator|.
name|filterTypeInOutputs
import|;
end_import

begin_class
DECL|class|CreateModelFromXmlTest
specifier|public
class|class
name|CreateModelFromXmlTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|NS_CAMEL
specifier|public
specifier|static
specifier|final
name|String
name|NS_CAMEL
init|=
literal|"http://camel.apache.org/schema/spring"
decl_stmt|;
DECL|field|NS_FOO
specifier|public
specifier|static
specifier|final
name|String
name|NS_FOO
init|=
literal|"http://foo"
decl_stmt|;
DECL|field|NS_BAR
specifier|public
specifier|static
specifier|final
name|String
name|NS_BAR
init|=
literal|"http://bar"
decl_stmt|;
annotation|@
name|Test
DECL|method|testCreateModelFromXmlForInputStreamWithDefaultNamespace ()
specifier|public
name|void
name|testCreateModelFromXmlForInputStreamWithDefaultNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|RoutesDefinition
name|routesDefinition
init|=
name|createModelFromXml
argument_list|(
literal|"simpleRoute.xml"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|routesDefinition
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expectedNamespaces
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|expectedNamespaces
operator|.
name|put
argument_list|(
literal|"xmlns"
argument_list|,
name|NS_CAMEL
argument_list|)
expr_stmt|;
name|assertNamespacesPresent
argument_list|(
name|routesDefinition
argument_list|,
name|expectedNamespaces
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateModelFromXmlForInputStreamWithAdditionalNamespaces ()
specifier|public
name|void
name|testCreateModelFromXmlForInputStreamWithAdditionalNamespaces
parameter_list|()
throws|throws
name|Exception
block|{
name|RoutesDefinition
name|routesDefinition
init|=
name|createModelFromXml
argument_list|(
literal|"simpleRouteWithNamespaces.xml"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|routesDefinition
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expectedNamespaces
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|expectedNamespaces
operator|.
name|put
argument_list|(
literal|"xmlns"
argument_list|,
name|NS_CAMEL
argument_list|)
expr_stmt|;
name|expectedNamespaces
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
name|NS_FOO
argument_list|)
expr_stmt|;
name|expectedNamespaces
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
name|NS_BAR
argument_list|)
expr_stmt|;
name|assertNamespacesPresent
argument_list|(
name|routesDefinition
argument_list|,
name|expectedNamespaces
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateModelFromXmlForStringWithDefaultNamespace ()
specifier|public
name|void
name|testCreateModelFromXmlForStringWithDefaultNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|RoutesDefinition
name|routesDefinition
init|=
name|createModelFromXml
argument_list|(
literal|"simpleRoute.xml"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|routesDefinition
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expectedNamespaces
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|expectedNamespaces
operator|.
name|put
argument_list|(
literal|"xmlns"
argument_list|,
name|NS_CAMEL
argument_list|)
expr_stmt|;
name|assertNamespacesPresent
argument_list|(
name|routesDefinition
argument_list|,
name|expectedNamespaces
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateModelFromXmlForStringWithAdditionalNamespaces ()
specifier|public
name|void
name|testCreateModelFromXmlForStringWithAdditionalNamespaces
parameter_list|()
throws|throws
name|Exception
block|{
name|RoutesDefinition
name|routesDefinition
init|=
name|createModelFromXml
argument_list|(
literal|"simpleRouteWithNamespaces.xml"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|routesDefinition
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expectedNamespaces
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|expectedNamespaces
operator|.
name|put
argument_list|(
literal|"xmlns"
argument_list|,
name|NS_CAMEL
argument_list|)
expr_stmt|;
name|expectedNamespaces
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
name|NS_FOO
argument_list|)
expr_stmt|;
name|expectedNamespaces
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
name|NS_BAR
argument_list|)
expr_stmt|;
name|assertNamespacesPresent
argument_list|(
name|routesDefinition
argument_list|,
name|expectedNamespaces
argument_list|)
expr_stmt|;
block|}
DECL|method|createModelFromXml (String camelContextResource, boolean fromString)
specifier|private
name|RoutesDefinition
name|createModelFromXml
parameter_list|(
name|String
name|camelContextResource
parameter_list|,
name|boolean
name|fromString
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|inputStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|camelContextResource
argument_list|)
decl_stmt|;
if|if
condition|(
name|fromString
condition|)
block|{
name|String
name|xml
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|inputStream
argument_list|)
decl_stmt|;
return|return
name|ModelHelper
operator|.
name|createModelFromXml
argument_list|(
name|context
argument_list|,
name|xml
argument_list|,
name|RoutesDefinition
operator|.
name|class
argument_list|)
return|;
block|}
return|return
name|ModelHelper
operator|.
name|createModelFromXml
argument_list|(
name|context
argument_list|,
name|inputStream
argument_list|,
name|RoutesDefinition
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|assertNamespacesPresent (RoutesDefinition routesDefinition, Map<String, String> expectedNamespaces)
specifier|private
name|void
name|assertNamespacesPresent
parameter_list|(
name|RoutesDefinition
name|routesDefinition
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expectedNamespaces
parameter_list|)
block|{
for|for
control|(
name|RouteDefinition
name|route
range|:
name|routesDefinition
operator|.
name|getRoutes
argument_list|()
control|)
block|{
name|Iterator
argument_list|<
name|ExpressionNode
argument_list|>
name|it
init|=
name|filterTypeInOutputs
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|ExpressionNode
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ExpressionNode
name|en
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ExpressionDefinition
name|ed
init|=
name|en
operator|.
name|getExpression
argument_list|()
decl_stmt|;
name|NamespaceAware
name|na
init|=
literal|null
decl_stmt|;
name|Expression
name|exp
init|=
name|ed
operator|.
name|getExpressionValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|exp
operator|instanceof
name|NamespaceAware
condition|)
block|{
name|na
operator|=
operator|(
name|NamespaceAware
operator|)
name|exp
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ed
operator|instanceof
name|NamespaceAware
condition|)
block|{
name|na
operator|=
operator|(
name|NamespaceAware
operator|)
name|ed
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|na
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedNamespaces
argument_list|,
name|na
operator|.
name|getNamespaces
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"Expected to find at least one ExpressionNode in route"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

