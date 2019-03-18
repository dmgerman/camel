begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.mail.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
operator|.
name|internal
package|;
end_package

begin_comment
comment|/**  * Constants for GoogleMail component.  */
end_comment

begin_class
DECL|class|GoogleMailConstants
specifier|public
specifier|final
class|class
name|GoogleMailConstants
block|{
comment|// suffix for parameters when passed as exchange header properties
DECL|field|PROPERTY_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY_PREFIX
init|=
literal|"CamelGoogleMail."
decl_stmt|;
comment|// thread profile name for this component
DECL|field|THREAD_PROFILE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|THREAD_PROFILE_NAME
init|=
literal|"CamelGoogleMail"
decl_stmt|;
comment|/**      * Prevent instantiation.      */
DECL|method|GoogleMailConstants ()
specifier|private
name|GoogleMailConstants
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

