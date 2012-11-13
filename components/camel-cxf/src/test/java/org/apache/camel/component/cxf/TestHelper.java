begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|TestHelper
specifier|public
specifier|final
class|class
name|TestHelper
block|{
DECL|method|TestHelper ()
specifier|private
name|TestHelper
parameter_list|()
block|{     }
comment|/**      * Is this OS the given platform.      *<p/>      * Uses<tt>os.name</tt> from the system properties to determine the OS.      *      * @param platform such as Windows      * @return<tt>true</tt> if its that platform.      */
DECL|method|isPlatform (String platform)
specifier|public
specifier|static
name|boolean
name|isPlatform
parameter_list|(
name|String
name|platform
parameter_list|)
block|{
name|String
name|osName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
return|return
name|osName
operator|.
name|indexOf
argument_list|(
name|platform
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
operator|>
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit

