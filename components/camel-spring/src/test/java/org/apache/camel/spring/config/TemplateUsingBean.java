begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
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
name|ProducerTemplate
import|;
end_import

begin_class
DECL|class|TemplateUsingBean
specifier|public
class|class
name|TemplateUsingBean
block|{
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
DECL|method|getTemplate ()
specifier|public
name|ProducerTemplate
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
DECL|method|setTemplate (ProducerTemplate template)
specifier|public
name|void
name|setTemplate
parameter_list|(
name|ProducerTemplate
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
block|}
end_class

end_unit

