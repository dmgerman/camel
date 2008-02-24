begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|XmlAccessType
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
literal|"header"
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
DECL|class|StringHeader
specifier|public
class|class
name|StringHeader
extends|extends
name|HeaderType
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"value"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|text
specifier|private
name|String
name|text
decl_stmt|;
DECL|method|StringHeader ()
specifier|public
name|StringHeader
parameter_list|()
block|{     }
DECL|method|StringHeader (String name, String text)
specifier|public
name|StringHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|text
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
DECL|method|setText (String text)
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|getText
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
name|String
condition|)
block|{
name|setText
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value must be a String"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

