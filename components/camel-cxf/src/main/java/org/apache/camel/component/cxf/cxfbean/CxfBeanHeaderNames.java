begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.cxfbean
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
name|cxfbean
package|;
end_package

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|CxfBeanHeaderNames
specifier|public
interface|interface
name|CxfBeanHeaderNames
block|{
DECL|field|VERB
name|String
name|VERB
init|=
literal|"CamelCxfBeanVerb"
decl_stmt|;
DECL|field|CONTENT_TYPE
name|String
name|CONTENT_TYPE
init|=
literal|"CamelCxfBeanContextType"
decl_stmt|;
DECL|field|CHARACTER_ENCODING
name|String
name|CHARACTER_ENCODING
init|=
literal|"CamelCxfBeanCharacterEncoding"
decl_stmt|;
DECL|field|PATH
name|String
name|PATH
init|=
literal|"CamelCxfBeanRequestPath"
decl_stmt|;
DECL|field|BASE_PATH
name|String
name|BASE_PATH
init|=
literal|"CamelCxfBeanRequestBasePath"
decl_stmt|;
block|}
end_interface

end_unit

