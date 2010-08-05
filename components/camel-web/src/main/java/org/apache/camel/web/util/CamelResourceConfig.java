begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|core
operator|.
name|PackagesResourceConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|core
operator|.
name|ResourceConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|wadl
operator|.
name|config
operator|.
name|WadlGeneratorConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|server
operator|.
name|wadl
operator|.
name|generators
operator|.
name|WadlGeneratorApplicationDoc
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|server
operator|.
name|wadl
operator|.
name|generators
operator|.
name|WadlGeneratorGrammarsSupport
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|server
operator|.
name|wadl
operator|.
name|generators
operator|.
name|resourcedoc
operator|.
name|WadlGeneratorResourceDocSupport
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
name|web
operator|.
name|resources
operator|.
name|Constants
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelResourceConfig
specifier|public
class|class
name|CamelResourceConfig
extends|extends
name|PackagesResourceConfig
block|{
DECL|method|CamelResourceConfig ()
specifier|public
name|CamelResourceConfig
parameter_list|()
block|{
name|this
argument_list|(
literal|"org.apache.camel.web"
argument_list|)
expr_stmt|;
block|}
DECL|method|CamelResourceConfig (String packages)
specifier|public
name|CamelResourceConfig
parameter_list|(
name|String
name|packages
parameter_list|)
block|{
name|super
argument_list|(
name|createProperties
argument_list|(
name|packages
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createProperties (String packages)
specifier|protected
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createProperties
parameter_list|(
name|String
name|packages
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|PackagesResourceConfig
operator|.
name|PROPERTY_PACKAGES
argument_list|,
name|packages
argument_list|)
expr_stmt|;
comment|/*         WadlGeneratorConfig config = WadlGeneratorConfig                 .generator(WadlGeneratorApplicationDoc.class)                 .prop("applicationDocsFile", "classpath:/application-doc.xml")                 .generator(WadlGeneratorGrammarsSupport.class)                 .prop("grammarsFile", "classpath:/application-grammars.xml")                 .generator(WadlGeneratorResourceDocSupport.class)                 .prop("resourceDocFile", "classpath:/resourcedoc.xml")                 .build();          properties.put(ResourceConfig.PROPERTY_WADL_GENERATOR_CONFIG, config); */
return|return
name|properties
return|;
block|}
DECL|method|getMediaTypeMappings ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|MediaType
argument_list|>
name|getMediaTypeMappings
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|MediaType
argument_list|>
name|m
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|MediaType
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"html"
argument_list|,
name|MediaType
operator|.
name|TEXT_HTML_TYPE
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"xml"
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"json"
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"dot"
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
name|Constants
operator|.
name|DOT_MIMETYPE
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
end_class

end_unit

