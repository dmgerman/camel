begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|issues
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
name|Produce
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CoolService
specifier|public
class|class
name|CoolService
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|stuff
specifier|private
name|MyCoolStuff
name|stuff
decl_stmt|;
DECL|method|stuff (String s)
specifier|public
name|String
name|stuff
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|stuff
operator|.
name|cool
argument_list|(
name|s
argument_list|)
return|;
block|}
DECL|method|split (String s)
specifier|public
name|String
index|[]
name|split
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|.
name|split
argument_list|(
literal|","
argument_list|)
return|;
block|}
block|}
end_class

end_unit

