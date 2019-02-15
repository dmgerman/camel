begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementWrapper
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|Metadata
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
name|util
operator|.
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * To specify the rest operation parameters using Swagger.  *<p/>  * This maps to the Swagger Parameter Message Object.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"rest"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"param"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RestOperationParamDefinition
specifier|public
class|class
name|RestOperationParamDefinition
block|{
annotation|@
name|XmlTransient
DECL|field|verb
specifier|private
name|VerbDefinition
name|verb
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"path"
argument_list|)
DECL|field|type
specifier|private
name|RestParamType
name|type
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|""
argument_list|)
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|""
argument_list|)
DECL|field|defaultValue
specifier|private
name|String
name|defaultValue
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|required
specifier|private
name|Boolean
name|required
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"csv"
argument_list|)
DECL|field|collectionFormat
specifier|private
name|CollectionFormat
name|collectionFormat
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"string"
argument_list|)
DECL|field|arrayType
specifier|private
name|String
name|arrayType
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"string"
argument_list|)
DECL|field|dataType
specifier|private
name|String
name|dataType
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|dataFormat
specifier|private
name|String
name|dataFormat
decl_stmt|;
annotation|@
name|XmlElementWrapper
argument_list|(
name|name
operator|=
literal|"allowableValues"
argument_list|)
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"value"
argument_list|)
DECL|field|allowableValues
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|allowableValues
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"examples"
argument_list|)
DECL|field|examples
specifier|private
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|examples
decl_stmt|;
DECL|method|RestOperationParamDefinition ()
specifier|public
name|RestOperationParamDefinition
parameter_list|()
block|{     }
DECL|method|RestOperationParamDefinition (VerbDefinition verb)
specifier|public
name|RestOperationParamDefinition
parameter_list|(
name|VerbDefinition
name|verb
parameter_list|)
block|{
name|this
operator|.
name|verb
operator|=
name|verb
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|RestParamType
name|getType
parameter_list|()
block|{
return|return
name|type
operator|!=
literal|null
condition|?
name|type
else|:
name|RestParamType
operator|.
name|path
return|;
block|}
comment|/**      * Sets the Swagger Parameter type.      */
DECL|method|setType (RestParamType type)
specifier|public
name|void
name|setType
parameter_list|(
name|RestParamType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
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
comment|/**      * Sets the Swagger Parameter name.      */
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
operator|!=
literal|null
condition|?
name|description
else|:
literal|""
return|;
block|}
comment|/**      * Sets the Swagger Parameter description.      */
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
comment|/**      * Sets the Swagger Parameter default value.      */
DECL|method|getDefaultValue ()
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
return|return
name|defaultValue
operator|!=
literal|null
condition|?
name|defaultValue
else|:
literal|""
return|;
block|}
DECL|method|setDefaultValue (String defaultValue)
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
name|defaultValue
parameter_list|)
block|{
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
block|}
DECL|method|getRequired ()
specifier|public
name|Boolean
name|getRequired
parameter_list|()
block|{
return|return
name|required
operator|!=
literal|null
condition|?
name|required
else|:
literal|true
return|;
block|}
comment|/**      * Sets the Swagger Parameter required flag.      */
DECL|method|setRequired (Boolean required)
specifier|public
name|void
name|setRequired
parameter_list|(
name|Boolean
name|required
parameter_list|)
block|{
name|this
operator|.
name|required
operator|=
name|required
expr_stmt|;
block|}
DECL|method|getCollectionFormat ()
specifier|public
name|CollectionFormat
name|getCollectionFormat
parameter_list|()
block|{
return|return
name|collectionFormat
return|;
block|}
comment|/**      * Sets the Swagger Parameter collection format.      */
DECL|method|setCollectionFormat (CollectionFormat collectionFormat)
specifier|public
name|void
name|setCollectionFormat
parameter_list|(
name|CollectionFormat
name|collectionFormat
parameter_list|)
block|{
name|this
operator|.
name|collectionFormat
operator|=
name|collectionFormat
expr_stmt|;
block|}
DECL|method|getArrayType ()
specifier|public
name|String
name|getArrayType
parameter_list|()
block|{
return|return
name|arrayType
return|;
block|}
comment|/**      * Sets the Swagger Parameter array type.      * Required if data type is "array". Describes the type of items in the array.      */
DECL|method|setArrayType (String arrayType)
specifier|public
name|void
name|setArrayType
parameter_list|(
name|String
name|arrayType
parameter_list|)
block|{
name|this
operator|.
name|arrayType
operator|=
name|arrayType
expr_stmt|;
block|}
DECL|method|getDataType ()
specifier|public
name|String
name|getDataType
parameter_list|()
block|{
return|return
name|dataType
operator|!=
literal|null
condition|?
name|dataType
else|:
literal|"string"
return|;
block|}
comment|/**      * Sets the Swagger Parameter data type.      */
DECL|method|setDataType (String dataType)
specifier|public
name|void
name|setDataType
parameter_list|(
name|String
name|dataType
parameter_list|)
block|{
name|this
operator|.
name|dataType
operator|=
name|dataType
expr_stmt|;
block|}
DECL|method|getDataFormat ()
specifier|public
name|String
name|getDataFormat
parameter_list|()
block|{
return|return
name|dataFormat
return|;
block|}
comment|/**      * Sets the Swagger Parameter data format.      */
DECL|method|setDataFormat (String dataFormat)
specifier|public
name|void
name|setDataFormat
parameter_list|(
name|String
name|dataFormat
parameter_list|)
block|{
name|this
operator|.
name|dataFormat
operator|=
name|dataFormat
expr_stmt|;
block|}
DECL|method|getAllowableValues ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAllowableValues
parameter_list|()
block|{
if|if
condition|(
name|allowableValues
operator|!=
literal|null
condition|)
block|{
return|return
name|allowableValues
return|;
block|}
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
comment|/**      * Sets the Swagger Parameter list of allowable values (enum).      */
DECL|method|setAllowableValues (List<String> allowableValues)
specifier|public
name|void
name|setAllowableValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|allowableValues
parameter_list|)
block|{
name|this
operator|.
name|allowableValues
operator|=
name|allowableValues
expr_stmt|;
block|}
DECL|method|getExamples ()
specifier|public
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|getExamples
parameter_list|()
block|{
return|return
name|examples
return|;
block|}
comment|/**      * Sets the Swagger Parameter examples.      */
DECL|method|setExamples (List<RestPropertyDefinition> examples)
specifier|public
name|void
name|setExamples
parameter_list|(
name|List
argument_list|<
name|RestPropertyDefinition
argument_list|>
name|examples
parameter_list|)
block|{
name|this
operator|.
name|examples
operator|=
name|examples
expr_stmt|;
block|}
comment|/**      * Name of the parameter.      *<p/>      * This option is mandatory.      */
DECL|method|name (String name)
specifier|public
name|RestOperationParamDefinition
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Description of the parameter.      */
DECL|method|description (String name)
specifier|public
name|RestOperationParamDefinition
name|description
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setDescription
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The default value of the parameter.      */
DECL|method|defaultValue (String name)
specifier|public
name|RestOperationParamDefinition
name|defaultValue
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setDefaultValue
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether the parameter is required      */
DECL|method|required (Boolean required)
specifier|public
name|RestOperationParamDefinition
name|required
parameter_list|(
name|Boolean
name|required
parameter_list|)
block|{
name|setRequired
argument_list|(
name|required
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the collection format.      */
DECL|method|collectionFormat (CollectionFormat collectionFormat)
specifier|public
name|RestOperationParamDefinition
name|collectionFormat
parameter_list|(
name|CollectionFormat
name|collectionFormat
parameter_list|)
block|{
name|setCollectionFormat
argument_list|(
name|collectionFormat
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The data type of the array data type      */
DECL|method|arrayType (String arrayType)
specifier|public
name|RestOperationParamDefinition
name|arrayType
parameter_list|(
name|String
name|arrayType
parameter_list|)
block|{
name|setArrayType
argument_list|(
name|arrayType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The data type of the parameter such as<tt>string</tt>,<tt>integer</tt>,<tt>boolean</tt>      */
DECL|method|dataType (String type)
specifier|public
name|RestOperationParamDefinition
name|dataType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setDataType
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The data format of the parameter such as<tt>binary</tt>,<tt>date</tt>,<tt>date-time</tt>,<tt>password</tt>.      * The format is usually derived from the dataType alone. However you can set this option for more fine grained control      * of the format in use.      */
DECL|method|dataFormat (String type)
specifier|public
name|RestOperationParamDefinition
name|dataFormat
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setDataFormat
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Allowed values of the parameter when its an enum type      */
DECL|method|allowableValues (List<String> allowableValues)
specifier|public
name|RestOperationParamDefinition
name|allowableValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|allowableValues
parameter_list|)
block|{
name|setAllowableValues
argument_list|(
name|allowableValues
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Allowed values of the parameter when its an enum type      */
DECL|method|allowableValues (String... allowableValues)
specifier|public
name|RestOperationParamDefinition
name|allowableValues
parameter_list|(
name|String
modifier|...
name|allowableValues
parameter_list|)
block|{
name|setAllowableValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|allowableValues
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Allowed values of the parameter when its an enum type      */
DECL|method|allowableValues (String allowableValues)
specifier|public
name|RestOperationParamDefinition
name|allowableValues
parameter_list|(
name|String
name|allowableValues
parameter_list|)
block|{
name|setAllowableValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|allowableValues
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The parameter type such as body, form, header, path, query      */
DECL|method|type (RestParamType type)
specifier|public
name|RestOperationParamDefinition
name|type
parameter_list|(
name|RestParamType
name|type
parameter_list|)
block|{
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a body example with the given content-type      */
DECL|method|example (String contentType, String example)
specifier|public
name|RestOperationParamDefinition
name|example
parameter_list|(
name|String
name|contentType
parameter_list|,
name|String
name|example
parameter_list|)
block|{
if|if
condition|(
name|examples
operator|==
literal|null
condition|)
block|{
name|examples
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|examples
operator|.
name|add
argument_list|(
operator|new
name|RestPropertyDefinition
argument_list|(
name|contentType
argument_list|,
name|example
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a single example      */
DECL|method|example (String example)
specifier|public
name|RestOperationParamDefinition
name|example
parameter_list|(
name|String
name|example
parameter_list|)
block|{
if|if
condition|(
name|examples
operator|==
literal|null
condition|)
block|{
name|examples
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|examples
operator|.
name|add
argument_list|(
operator|new
name|RestPropertyDefinition
argument_list|(
literal|""
argument_list|,
name|example
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Ends the configuration of this parameter      */
DECL|method|endParam ()
specifier|public
name|RestDefinition
name|endParam
parameter_list|()
block|{
comment|// name is mandatory
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|verb
operator|.
name|getParams
argument_list|()
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|verb
operator|.
name|getRest
argument_list|()
return|;
block|}
block|}
end_class

end_unit

