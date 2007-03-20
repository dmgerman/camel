begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Headers
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|HeadersSupport
specifier|public
specifier|abstract
class|class
name|HeadersSupport
implements|implements
name|Headers
block|{
DECL|method|copy ()
specifier|public
name|Headers
name|copy
parameter_list|()
block|{
name|Headers
name|answer
init|=
name|newInstance
argument_list|()
decl_stmt|;
name|answer
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|newInstance ()
specifier|public
name|Headers
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|DefaultHeaders
argument_list|()
return|;
block|}
block|}
end_class

end_unit

