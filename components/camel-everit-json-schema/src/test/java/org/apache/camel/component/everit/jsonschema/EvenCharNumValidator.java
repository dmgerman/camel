begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.everit.jsonschema
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|everit
operator|.
name|jsonschema
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|everit
operator|.
name|json
operator|.
name|schema
operator|.
name|FormatValidator
import|;
end_import

begin_class
DECL|class|EvenCharNumValidator
specifier|public
class|class
name|EvenCharNumValidator
implements|implements
name|FormatValidator
block|{
annotation|@
name|Override
DECL|method|validate (final String subject)
specifier|public
name|Optional
argument_list|<
name|String
argument_list|>
name|validate
parameter_list|(
specifier|final
name|String
name|subject
parameter_list|)
block|{
if|if
condition|(
name|subject
operator|.
name|length
argument_list|()
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"the length of string [%s] is odd"
argument_list|,
name|subject
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|formatName ()
specifier|public
name|String
name|formatName
parameter_list|()
block|{
return|return
literal|"evenlength"
return|;
block|}
block|}
end_class

end_unit

