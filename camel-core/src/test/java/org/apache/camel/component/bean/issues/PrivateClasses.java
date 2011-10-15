begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|issues
package|;
end_package

begin_comment
comment|/**  * Holds Private classes to be bean-binded through their interface  */
end_comment

begin_class
DECL|class|PrivateClasses
specifier|public
specifier|final
class|class
name|PrivateClasses
block|{
DECL|field|EXPECTED_OUTPUT
specifier|public
specifier|static
specifier|final
name|String
name|EXPECTED_OUTPUT
init|=
literal|"Hello Camel"
decl_stmt|;
DECL|field|METHOD_NAME
specifier|public
specifier|static
specifier|final
name|String
name|METHOD_NAME
init|=
literal|"sayHello"
decl_stmt|;
DECL|method|PrivateClasses ()
specifier|private
name|PrivateClasses
parameter_list|()
block|{
comment|// Utility class; can't be instantiated
block|}
comment|/**      * Public interface through which we can bean-bind a private impl      */
DECL|interface|HelloCamel
specifier|public
specifier|static
interface|interface
name|HelloCamel
block|{
DECL|method|sayHello (String input)
name|String
name|sayHello
parameter_list|(
name|String
name|input
parameter_list|)
function_decl|;
block|}
DECL|class|PackagePrivateHelloCamel
specifier|static
specifier|final
class|class
name|PackagePrivateHelloCamel
implements|implements
name|HelloCamel
block|{
annotation|@
name|Override
DECL|method|sayHello (String input)
specifier|public
name|String
name|sayHello
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|EXPECTED_OUTPUT
return|;
block|}
block|}
DECL|class|PrivateHelloCamel
specifier|private
specifier|static
specifier|final
class|class
name|PrivateHelloCamel
implements|implements
name|HelloCamel
block|{
annotation|@
name|Override
DECL|method|sayHello (String input)
specifier|public
name|String
name|sayHello
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|EXPECTED_OUTPUT
return|;
block|}
block|}
comment|/**      * @return package-private implementation that can only be bean-binded      *         through its interface      */
DECL|method|newPackagePrivateHelloCamel ()
specifier|public
specifier|static
name|HelloCamel
name|newPackagePrivateHelloCamel
parameter_list|()
block|{
return|return
operator|new
name|PackagePrivateHelloCamel
argument_list|()
return|;
block|}
comment|/**      * @return private implementation that can only be bean-binded through its      *         interface      */
DECL|method|newPrivateHelloCamel ()
specifier|public
specifier|static
name|HelloCamel
name|newPrivateHelloCamel
parameter_list|()
block|{
return|return
operator|new
name|PrivateHelloCamel
argument_list|()
return|;
block|}
block|}
end_class

end_unit

