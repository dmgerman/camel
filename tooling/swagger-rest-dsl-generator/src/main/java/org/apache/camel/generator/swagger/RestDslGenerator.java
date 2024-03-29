begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.generator.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|generator
operator|.
name|swagger
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|processing
operator|.
name|Filer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Swagger
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
name|rest
operator|.
name|RestsDefinition
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
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * Source code and {@link RestsDefinition} generator that generates Camel REST  * DSL implementations from Swagger (OpenAPI) specifications.  */
end_comment

begin_class
DECL|class|RestDslGenerator
specifier|public
specifier|abstract
class|class
name|RestDslGenerator
parameter_list|<
name|G
parameter_list|>
block|{
DECL|field|swagger
specifier|final
name|Swagger
name|swagger
decl_stmt|;
DECL|field|destinationGenerator
name|DestinationGenerator
name|destinationGenerator
init|=
operator|new
name|DirectToOperationId
argument_list|()
decl_stmt|;
DECL|field|filter
name|OperationFilter
name|filter
init|=
operator|new
name|OperationFilter
argument_list|()
decl_stmt|;
DECL|field|restComponent
name|String
name|restComponent
decl_stmt|;
DECL|field|restContextPath
name|String
name|restContextPath
decl_stmt|;
DECL|field|apiContextPath
name|String
name|apiContextPath
decl_stmt|;
DECL|field|springComponent
name|boolean
name|springComponent
decl_stmt|;
DECL|field|springBootProject
name|boolean
name|springBootProject
decl_stmt|;
DECL|method|RestDslGenerator (final Swagger swagger)
name|RestDslGenerator
parameter_list|(
specifier|final
name|Swagger
name|swagger
parameter_list|)
block|{
name|this
operator|.
name|swagger
operator|=
name|notNull
argument_list|(
name|swagger
argument_list|,
literal|"swagger"
argument_list|)
expr_stmt|;
block|}
DECL|method|withDestinationGenerator (final DestinationGenerator directRouteGenerator)
specifier|public
name|G
name|withDestinationGenerator
parameter_list|(
specifier|final
name|DestinationGenerator
name|directRouteGenerator
parameter_list|)
block|{
name|notNull
argument_list|(
name|directRouteGenerator
argument_list|,
literal|"directRouteGenerator"
argument_list|)
expr_stmt|;
name|this
operator|.
name|destinationGenerator
operator|=
name|directRouteGenerator
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|G
name|that
init|=
operator|(
name|G
operator|)
name|this
decl_stmt|;
return|return
name|that
return|;
block|}
DECL|method|destinationGenerator ()
name|DestinationGenerator
name|destinationGenerator
parameter_list|()
block|{
return|return
name|destinationGenerator
return|;
block|}
DECL|method|withOperationFilter (OperationFilter filter)
specifier|public
name|G
name|withOperationFilter
parameter_list|(
name|OperationFilter
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|G
name|that
init|=
operator|(
name|G
operator|)
name|this
decl_stmt|;
return|return
name|that
return|;
block|}
DECL|method|withOperationFilter (String include)
specifier|public
name|G
name|withOperationFilter
parameter_list|(
name|String
name|include
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|.
name|setIncludes
argument_list|(
name|include
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|G
name|that
init|=
operator|(
name|G
operator|)
name|this
decl_stmt|;
return|return
name|that
return|;
block|}
DECL|method|withRestComponent (String restComponent)
specifier|public
name|G
name|withRestComponent
parameter_list|(
name|String
name|restComponent
parameter_list|)
block|{
name|this
operator|.
name|restComponent
operator|=
name|restComponent
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|G
name|that
init|=
operator|(
name|G
operator|)
name|this
decl_stmt|;
return|return
name|that
return|;
block|}
DECL|method|withRestContextPath (String contextPath)
specifier|public
name|G
name|withRestContextPath
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|this
operator|.
name|restContextPath
operator|=
name|contextPath
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|G
name|that
init|=
operator|(
name|G
operator|)
name|this
decl_stmt|;
return|return
name|that
return|;
block|}
DECL|method|withApiContextPath (String contextPath)
specifier|public
name|G
name|withApiContextPath
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|this
operator|.
name|apiContextPath
operator|=
name|contextPath
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|G
name|that
init|=
operator|(
name|G
operator|)
name|this
decl_stmt|;
return|return
name|that
return|;
block|}
DECL|method|asSpringComponent ()
specifier|public
name|G
name|asSpringComponent
parameter_list|()
block|{
name|this
operator|.
name|springComponent
operator|=
literal|true
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|G
name|that
init|=
operator|(
name|G
operator|)
name|this
decl_stmt|;
return|return
name|that
return|;
block|}
DECL|method|asSpringBootProject ()
specifier|public
name|G
name|asSpringBootProject
parameter_list|()
block|{
name|this
operator|.
name|springBootProject
operator|=
literal|true
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|G
name|that
init|=
operator|(
name|G
operator|)
name|this
decl_stmt|;
return|return
name|that
return|;
block|}
DECL|method|toAppendable (final Swagger swagger)
specifier|public
specifier|static
name|RestDslSourceCodeGenerator
argument_list|<
name|Appendable
argument_list|>
name|toAppendable
parameter_list|(
specifier|final
name|Swagger
name|swagger
parameter_list|)
block|{
return|return
operator|new
name|AppendableGenerator
argument_list|(
name|swagger
argument_list|)
return|;
block|}
DECL|method|toDefinition (final Swagger swagger)
specifier|public
specifier|static
name|RestDslDefinitionGenerator
name|toDefinition
parameter_list|(
specifier|final
name|Swagger
name|swagger
parameter_list|)
block|{
return|return
operator|new
name|RestDslDefinitionGenerator
argument_list|(
name|swagger
argument_list|)
return|;
block|}
DECL|method|toXml (final Swagger swagger)
specifier|public
specifier|static
name|RestDslXmlGenerator
name|toXml
parameter_list|(
specifier|final
name|Swagger
name|swagger
parameter_list|)
block|{
return|return
operator|new
name|RestDslXmlGenerator
argument_list|(
name|swagger
argument_list|)
return|;
block|}
DECL|method|toFiler (final Swagger swagger)
specifier|public
specifier|static
name|RestDslSourceCodeGenerator
argument_list|<
name|Filer
argument_list|>
name|toFiler
parameter_list|(
specifier|final
name|Swagger
name|swagger
parameter_list|)
block|{
return|return
operator|new
name|FilerGenerator
argument_list|(
name|swagger
argument_list|)
return|;
block|}
DECL|method|toPath (final Swagger swagger)
specifier|public
specifier|static
name|RestDslSourceCodeGenerator
argument_list|<
name|Path
argument_list|>
name|toPath
parameter_list|(
specifier|final
name|Swagger
name|swagger
parameter_list|)
block|{
return|return
operator|new
name|PathGenerator
argument_list|(
name|swagger
argument_list|)
return|;
block|}
block|}
end_class

end_unit

