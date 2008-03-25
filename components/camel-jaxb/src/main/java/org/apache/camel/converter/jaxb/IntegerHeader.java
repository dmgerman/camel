begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
package|;
end_package

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
name|XmlRootElement
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"intHeader"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|value
operator|=
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|IntegerHeader
specifier|public
class|class
name|IntegerHeader
extends|extends
name|HeaderType
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"value"
argument_list|)
DECL|field|number
specifier|private
name|Integer
name|number
decl_stmt|;
DECL|method|IntegerHeader ()
specifier|public
name|IntegerHeader
parameter_list|()
block|{     }
DECL|method|IntegerHeader (String name, Integer number)
specifier|public
name|IntegerHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Integer
name|number
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|number
operator|=
name|number
expr_stmt|;
block|}
DECL|method|getNumber ()
specifier|public
name|Integer
name|getNumber
parameter_list|()
block|{
return|return
name|number
return|;
block|}
DECL|method|setNumber (Integer number)
specifier|public
name|void
name|setNumber
parameter_list|(
name|Integer
name|number
parameter_list|)
block|{
name|this
operator|.
name|number
operator|=
name|number
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|getNumber
argument_list|()
return|;
block|}
DECL|method|setValue (Object value)
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|Number
name|n
init|=
operator|(
name|Number
operator|)
name|value
decl_stmt|;
name|setNumber
argument_list|(
name|n
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value must be an Integer"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

