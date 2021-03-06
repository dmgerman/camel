begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|config
package|;
end_package

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
name|XmlAnyAttribute
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
name|XmlType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|OtherAttributesAware
import|;
end_import

begin_class
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"resequencerConfig"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ResequencerConfig
specifier|public
specifier|abstract
class|class
name|ResequencerConfig
implements|implements
name|OtherAttributesAware
block|{
comment|// use xs:any to support optional property placeholders
annotation|@
name|XmlAnyAttribute
DECL|field|otherAttributes
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|otherAttributes
decl_stmt|;
annotation|@
name|Override
DECL|method|getOtherAttributes ()
specifier|public
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|getOtherAttributes
parameter_list|()
block|{
return|return
name|otherAttributes
return|;
block|}
annotation|@
name|Override
DECL|method|setOtherAttributes (Map<QName, Object> otherAttributes)
specifier|public
name|void
name|setOtherAttributes
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|otherAttributes
parameter_list|)
block|{
name|this
operator|.
name|otherAttributes
operator|=
name|otherAttributes
expr_stmt|;
block|}
block|}
end_class

end_unit

