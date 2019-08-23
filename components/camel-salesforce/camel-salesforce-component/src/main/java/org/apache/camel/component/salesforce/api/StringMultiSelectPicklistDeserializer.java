begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonParseException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonParser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|BeanProperty
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|DeserializationContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|JsonDeserializer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|JsonMappingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|deser
operator|.
name|ContextualDeserializer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|deser
operator|.
name|std
operator|.
name|StdDeserializer
import|;
end_import

begin_comment
comment|/**  * Jackson deserializer base class for reading ';' separated strings for  * MultiSelect pick-lists.  */
end_comment

begin_class
DECL|class|StringMultiSelectPicklistDeserializer
specifier|public
class|class
name|StringMultiSelectPicklistDeserializer
extends|extends
name|StdDeserializer
argument_list|<
name|Object
argument_list|>
implements|implements
name|ContextualDeserializer
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7380774744798254325L
decl_stmt|;
DECL|method|StringMultiSelectPicklistDeserializer ()
specifier|public
name|StringMultiSelectPicklistDeserializer
parameter_list|()
block|{
name|super
argument_list|(
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|StringMultiSelectPicklistDeserializer (Class<?> vc)
specifier|protected
name|StringMultiSelectPicklistDeserializer
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|vc
parameter_list|)
block|{
name|super
argument_list|(
name|vc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deserialize (JsonParser jp, DeserializationContext ctxt)
specifier|public
name|Object
name|deserialize
parameter_list|(
name|JsonParser
name|jp
parameter_list|,
name|DeserializationContext
name|ctxt
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|String
name|listValue
init|=
name|jp
operator|.
name|getText
argument_list|()
decl_stmt|;
try|try
block|{
comment|// parse the string of the form value1;value2;...
specifier|final
name|String
index|[]
name|value
init|=
name|listValue
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
specifier|final
name|int
name|length
init|=
name|value
operator|.
name|length
decl_stmt|;
specifier|final
name|Object
name|resultArray
init|=
name|Array
operator|.
name|newInstance
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// use factory method to create object
name|Array
operator|.
name|set
argument_list|(
name|resultArray
argument_list|,
name|i
argument_list|,
name|value
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|resultArray
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JsonParseException
argument_list|(
name|jp
argument_list|,
literal|"Exception reading multi-select pick list value"
argument_list|,
name|jp
operator|.
name|getCurrentLocation
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createContextual (DeserializationContext context, BeanProperty property)
specifier|public
name|JsonDeserializer
argument_list|<
name|?
argument_list|>
name|createContextual
parameter_list|(
name|DeserializationContext
name|context
parameter_list|,
name|BeanProperty
name|property
parameter_list|)
throws|throws
name|JsonMappingException
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|rawClass
init|=
name|property
operator|.
name|getType
argument_list|()
operator|.
name|getRawClass
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|componentType
init|=
name|rawClass
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
if|if
condition|(
name|componentType
operator|==
literal|null
operator|||
name|componentType
operator|!=
name|String
operator|.
name|class
condition|)
block|{
throw|throw
operator|new
name|JsonMappingException
argument_list|(
name|context
operator|.
name|getParser
argument_list|()
argument_list|,
literal|"Pick list String array expected for "
operator|+
name|rawClass
argument_list|)
throw|;
block|}
return|return
operator|new
name|StringMultiSelectPicklistDeserializer
argument_list|(
name|rawClass
argument_list|)
return|;
block|}
block|}
end_class

end_unit

