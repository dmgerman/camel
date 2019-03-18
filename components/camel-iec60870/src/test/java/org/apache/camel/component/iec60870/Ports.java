begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_class
DECL|class|Ports
specifier|public
specifier|final
class|class
name|Ports
block|{
DECL|method|Ports ()
specifier|private
name|Ports
parameter_list|()
block|{     }
DECL|method|pickServerPort ()
specifier|public
specifier|static
name|int
name|pickServerPort
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|ServerSocket
name|socket
init|=
operator|new
name|ServerSocket
argument_list|(
literal|0
argument_list|)
init|)
block|{
return|return
name|socket
operator|.
name|getLocalPort
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

