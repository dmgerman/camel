begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.jettyproducer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
operator|.
name|jettyproducer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_class
DECL|class|MyInputStream
specifier|public
class|class
name|MyInputStream
extends|extends
name|ByteArrayInputStream
block|{
DECL|field|closed
specifier|private
name|boolean
name|closed
decl_stmt|;
DECL|method|MyInputStream (byte[] buf)
specifier|public
name|MyInputStream
parameter_list|(
name|byte
index|[]
name|buf
parameter_list|)
block|{
name|super
argument_list|(
name|buf
argument_list|)
expr_stmt|;
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|closed
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|isClosed ()
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|closed
return|;
block|}
block|}
end_class

end_unit

