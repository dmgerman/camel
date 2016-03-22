begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm.test.mocks.cmsender
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
operator|.
name|test
operator|.
name|mocks
operator|.
name|cmsender
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
name|component
operator|.
name|cm
operator|.
name|CMMessage
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
name|component
operator|.
name|cm
operator|.
name|CMSender
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
name|component
operator|.
name|cm
operator|.
name|exceptions
operator|.
name|cmresponse
operator|.
name|NoAccountFoundForProductTokenException
import|;
end_import

begin_class
DECL|class|NoAccountFoundForProductTokenExceptionSender
specifier|public
class|class
name|NoAccountFoundForProductTokenExceptionSender
implements|implements
name|CMSender
block|{
annotation|@
name|Override
DECL|method|send (CMMessage cmMessage)
specifier|public
name|void
name|send
parameter_list|(
name|CMMessage
name|cmMessage
parameter_list|)
block|{
throw|throw
operator|new
name|NoAccountFoundForProductTokenException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

