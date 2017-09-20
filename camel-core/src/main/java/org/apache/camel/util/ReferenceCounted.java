begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_class
DECL|class|ReferenceCounted
specifier|public
class|class
name|ReferenceCounted
block|{
DECL|field|count
specifier|private
specifier|final
name|ReferenceCount
name|count
decl_stmt|;
DECL|method|ReferenceCounted ()
specifier|protected
name|ReferenceCounted
parameter_list|()
block|{
name|this
operator|.
name|count
operator|=
name|ReferenceCount
operator|.
name|onRelease
argument_list|(
name|this
operator|::
name|doRelease
argument_list|)
expr_stmt|;
name|this
operator|.
name|count
operator|.
name|retain
argument_list|()
expr_stmt|;
block|}
DECL|method|retain ()
specifier|public
name|void
name|retain
parameter_list|()
throws|throws
name|IllegalStateException
block|{
name|count
operator|.
name|retain
argument_list|()
expr_stmt|;
block|}
DECL|method|release ()
specifier|public
name|void
name|release
parameter_list|()
throws|throws
name|IllegalStateException
block|{
name|count
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
comment|// cleanup
DECL|method|doRelease ()
specifier|protected
name|void
name|doRelease
parameter_list|()
block|{     }
block|}
end_class

end_unit

