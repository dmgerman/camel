begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.multipart
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
name|multipart
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cxf
operator|.
name|multipart
operator|.
name|MultiPartInvoke
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cxf
operator|.
name|multipart
operator|.
name|types
operator|.
name|InE
import|;
end_import

begin_class
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"MultiPartInvokeService"
argument_list|,
name|portName
operator|=
literal|"MultiPartInvokePort"
argument_list|,
name|targetNamespace
operator|=
literal|"http://adapter.ti.tongtech.com/ws"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.camel.cxf.multipart.MultiPartInvoke"
argument_list|)
DECL|class|MultiPartInvokeImpl
specifier|public
class|class
name|MultiPartInvokeImpl
implements|implements
name|MultiPartInvoke
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|MultiPartInvokeImpl
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|foo (InE in, InE in1, Holder<InE> out, Holder<InE> out1)
specifier|public
name|void
name|foo
parameter_list|(
name|InE
name|in
parameter_list|,
name|InE
name|in1
parameter_list|,
name|Holder
argument_list|<
name|InE
argument_list|>
name|out
parameter_list|,
name|Holder
argument_list|<
name|InE
argument_list|>
name|out1
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing operation foo"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|in1
argument_list|)
expr_stmt|;
try|try
block|{
name|InE
name|outValue
init|=
name|in
decl_stmt|;
name|out
operator|.
name|value
operator|=
name|outValue
expr_stmt|;
name|InE
name|out1Value
init|=
name|in1
decl_stmt|;
name|out1
operator|.
name|value
operator|=
name|out1Value
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

