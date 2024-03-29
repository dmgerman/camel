begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.feature
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|feature
package|;
end_package

begin_comment
comment|/**  *<p>  * MessageDataFormatFeature sets up the CXF endpoint interceptor for handling the  * Message in Message data format.  Only the interceptors of these phases are  *<b>preserved</b>:  *</p>  *<p>  * In phases: {Phase.RECEIVE , Phase.INVOKE, Phase.POST_INVOKE}  *</p>  *<p>  * Out phases: {Phase.PREPARE_SEND, Phase.WRITE, Phase.SEND, Phase.PREPARE_SEND_ENDING}  *</p>  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|MessageDataFormatFeature
specifier|public
class|class
name|MessageDataFormatFeature
extends|extends
name|RAWDataFormatFeature
block|{ }
end_class

end_unit

