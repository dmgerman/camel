begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cdi.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|test
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Alternative
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|Body
import|;
end_import

begin_class
annotation|@
name|Alternative
annotation|@
name|Named
argument_list|(
literal|"bean"
argument_list|)
DECL|class|AlternativeBean
specifier|public
class|class
name|AlternativeBean
block|{
DECL|method|process (@ody String body)
specifier|public
name|String
name|process
parameter_list|(
annotation|@
name|Body
name|String
name|body
parameter_list|)
block|{
return|return
name|body
operator|+
literal|" with alternative bean!"
return|;
block|}
block|}
end_class

end_unit

