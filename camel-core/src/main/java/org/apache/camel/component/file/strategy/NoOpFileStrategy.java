begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.strategy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|strategy
package|;
end_package

begin_comment
comment|/**  * A simple strategy which just locks the file but does not modify it  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|NoOpFileStrategy
specifier|public
class|class
name|NoOpFileStrategy
extends|extends
name|FileStategySupport
block|{
DECL|method|NoOpFileStrategy ()
specifier|public
name|NoOpFileStrategy
parameter_list|()
block|{     }
DECL|method|NoOpFileStrategy (boolean lockFile)
specifier|public
name|NoOpFileStrategy
parameter_list|(
name|boolean
name|lockFile
parameter_list|)
block|{
name|super
argument_list|(
name|lockFile
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

