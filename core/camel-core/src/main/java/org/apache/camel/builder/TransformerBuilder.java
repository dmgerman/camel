begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|CamelContext
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
name|DataFormatDefinition
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
name|ModelCamelContext
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
name|transformer
operator|.
name|CustomTransformerDefinition
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
name|transformer
operator|.
name|DataFormatTransformerDefinition
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
name|transformer
operator|.
name|EndpointTransformerDefinition
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
name|transformer
operator|.
name|TransformerDefinition
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
name|DataType
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
name|Transformer
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/dsl.html">Java DSL</a> which is  * used to build a {@link org.apache.camel.spi.Transformer} and register into {@link org.apache.camel.CamelContext}.  * It requires 'scheme' or a pair of 'from' and 'to' to be specified by scheme(), from() and to() method.  * And then you can choose a type of transformer by withUri(), withDataFormat(), withJava() or withBean() method.  */
end_comment

begin_class
DECL|class|TransformerBuilder
specifier|public
class|class
name|TransformerBuilder
block|{
DECL|field|scheme
specifier|private
name|String
name|scheme
decl_stmt|;
DECL|field|from
specifier|private
name|String
name|from
decl_stmt|;
DECL|field|to
specifier|private
name|String
name|to
decl_stmt|;
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|field|dataFormat
specifier|private
name|DataFormatDefinition
name|dataFormat
decl_stmt|;
DECL|field|clazz
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Transformer
argument_list|>
name|clazz
decl_stmt|;
DECL|field|beanRef
specifier|private
name|String
name|beanRef
decl_stmt|;
comment|/**      * Set the scheme name supported by the transformer.      * If you specify 'csv', the transformer will be picked up for all of 'csv' from/to      * Java transformation. Note that the scheme matching is performed only when      * no exactly matched transformer exists.      *      * @param scheme scheme name      */
DECL|method|scheme (String scheme)
specifier|public
name|TransformerBuilder
name|scheme
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
name|this
operator|.
name|scheme
operator|=
name|scheme
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the 'from' data type name.      * If you specify 'xml:XYZ', the transformer will be picked up if source type is      * 'xml:XYZ'. If you specify just 'xml', the transformer matches with all of      * 'xml' source type like 'xml:ABC' or 'xml:DEF'.      *      * @param from 'from' data type name      */
DECL|method|fromType (String from)
specifier|public
name|TransformerBuilder
name|fromType
parameter_list|(
name|String
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the 'from' data type using Java class.      *      * @param from 'from' Java class      */
DECL|method|fromType (Class<?> from)
specifier|public
name|TransformerBuilder
name|fromType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
operator|new
name|DataType
argument_list|(
name|from
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the 'to' data type name.      * If you specify 'json:XYZ', the transformer will be picked up if destination type is      * 'json:XYZ'. If you specify just 'json', the transformer matches with all of      * 'json' destination type like 'json:ABC' or 'json:DEF'.      *      * @param to 'to' data type      */
DECL|method|toType (String to)
specifier|public
name|TransformerBuilder
name|toType
parameter_list|(
name|String
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the 'to' data type using Java class.      *      * @param to 'to' Java class      */
DECL|method|toType (Class<?> to)
specifier|public
name|TransformerBuilder
name|toType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
operator|new
name|DataType
argument_list|(
name|to
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the URI to be used for the endpoint {@code Transformer}.      *      * @param uri endpoint URI      */
DECL|method|withUri (String uri)
specifier|public
name|TransformerBuilder
name|withUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|resetType
argument_list|()
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the {@code DataFormatDefinition} to be used for the {@code DataFormat} {@code Transformer}.      */
DECL|method|withDataFormat (DataFormatDefinition dataFormatDefinition)
specifier|public
name|TransformerBuilder
name|withDataFormat
parameter_list|(
name|DataFormatDefinition
name|dataFormatDefinition
parameter_list|)
block|{
name|resetType
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataFormat
operator|=
name|dataFormatDefinition
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the Java {@code Class} represents a custom {@code Transformer} implementation class.      */
DECL|method|withJava (Class<? extends Transformer> clazz)
specifier|public
name|TransformerBuilder
name|withJava
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Transformer
argument_list|>
name|clazz
parameter_list|)
block|{
name|resetType
argument_list|()
expr_stmt|;
name|this
operator|.
name|clazz
operator|=
name|clazz
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the Java Bean name to be used for custom {@code Transformer}.      */
DECL|method|withBean (String ref)
specifier|public
name|TransformerBuilder
name|withBean
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|resetType
argument_list|()
expr_stmt|;
name|this
operator|.
name|beanRef
operator|=
name|ref
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|resetType ()
specifier|private
name|void
name|resetType
parameter_list|()
block|{
name|this
operator|.
name|uri
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|dataFormat
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|clazz
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|beanRef
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Configure a Transformer according to the configurations built on this builder      * and register it into given {@code CamelContext}.      *       * @param camelContext {@code CamelContext}      */
DECL|method|configure (CamelContext camelContext)
specifier|public
name|void
name|configure
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|TransformerDefinition
name|transformer
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|EndpointTransformerDefinition
name|etd
init|=
operator|new
name|EndpointTransformerDefinition
argument_list|()
decl_stmt|;
name|etd
operator|.
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|transformer
operator|=
name|etd
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dataFormat
operator|!=
literal|null
condition|)
block|{
name|DataFormatTransformerDefinition
name|dtd
init|=
operator|new
name|DataFormatTransformerDefinition
argument_list|()
decl_stmt|;
name|dtd
operator|.
name|setDataFormatType
argument_list|(
name|dataFormat
argument_list|)
expr_stmt|;
name|transformer
operator|=
name|dtd
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|CustomTransformerDefinition
name|ctd
init|=
operator|new
name|CustomTransformerDefinition
argument_list|()
decl_stmt|;
name|ctd
operator|.
name|setClassName
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|transformer
operator|=
name|ctd
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|beanRef
operator|!=
literal|null
condition|)
block|{
name|CustomTransformerDefinition
name|ctd
init|=
operator|new
name|CustomTransformerDefinition
argument_list|()
decl_stmt|;
name|ctd
operator|.
name|setRef
argument_list|(
name|beanRef
argument_list|)
expr_stmt|;
name|transformer
operator|=
name|ctd
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No Transformer type was specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|scheme
operator|!=
literal|null
condition|)
block|{
name|transformer
operator|.
name|setScheme
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|transformer
operator|.
name|setFromType
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setToType
argument_list|(
name|to
argument_list|)
expr_stmt|;
block|}
name|camelContext
operator|.
name|adapt
argument_list|(
name|ModelCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getTransformers
argument_list|()
operator|.
name|add
argument_list|(
name|transformer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
