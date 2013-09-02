begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.facebook
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
package|;
end_package

begin_comment
comment|/**  * Common constants.  */
end_comment

begin_interface
DECL|interface|FacebookConstants
specifier|public
interface|interface
name|FacebookConstants
block|{
comment|// reading options property name and prefix for uri property
DECL|field|READING_PPROPERTY
name|String
name|READING_PPROPERTY
init|=
literal|"reading"
decl_stmt|;
DECL|field|READING_PREFIX
name|String
name|READING_PREFIX
init|=
name|READING_PPROPERTY
operator|+
literal|"."
decl_stmt|;
comment|// property name prefix for exchange 'in' headers
DECL|field|FACEBOOK_PROPERTY_PREFIX
name|String
name|FACEBOOK_PROPERTY_PREFIX
init|=
literal|"CamelFacebook."
decl_stmt|;
DECL|field|FACEBOOK_THREAD_PROFILE_NAME
name|String
name|FACEBOOK_THREAD_PROFILE_NAME
init|=
literal|"CamelFacebook"
decl_stmt|;
comment|// date format used by Facebook Reading since and until fields
DECL|field|FACEBOOK_DATE_FORMAT
name|String
name|FACEBOOK_DATE_FORMAT
init|=
literal|"yyyy-MM-dd'T'HH:mm:ssZ"
decl_stmt|;
block|}
end_interface

end_unit

