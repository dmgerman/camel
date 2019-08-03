begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.avro
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
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
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|jetty
operator|.
name|HttpServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|reflect
operator|.
name|ReflectResponder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|specific
operator|.
name|SpecificResponder
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
name|avro
operator|.
name|generated
operator|.
name|KeyValueProtocol
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
name|avro
operator|.
name|test
operator|.
name|TestReflection
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|AvroHttpProducerTest
specifier|public
class|class
name|AvroHttpProducerTest
extends|extends
name|AvroProducerTestSupport
block|{
annotation|@
name|Override
DECL|method|initializeServer ()
specifier|protected
name|void
name|initializeServer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|server
operator|=
operator|new
name|HttpServer
argument_list|(
operator|new
name|SpecificResponder
argument_list|(
name|KeyValueProtocol
operator|.
name|PROTOCOL
argument_list|,
name|keyValue
argument_list|)
argument_list|,
name|avroPort
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|serverReflection
operator|==
literal|null
condition|)
block|{
name|serverReflection
operator|=
operator|new
name|HttpServer
argument_list|(
operator|new
name|ReflectResponder
argument_list|(
name|TestReflection
operator|.
name|class
argument_list|,
name|testReflection
argument_list|)
argument_list|,
name|avroPortReflection
argument_list|)
expr_stmt|;
name|serverReflection
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|//In Only
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"avro:http:localhost:"
operator|+
name|avroPort
operator|+
literal|"?protocolClassName=org.apache.camel.avro.generated.KeyValueProtocol"
argument_list|)
expr_stmt|;
comment|//In Only with message in route
name|from
argument_list|(
literal|"direct:in-message-name"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:in-message-name-error"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"avro:http:localhost:"
operator|+
name|avroPort
operator|+
literal|"/put?protocolClassName=org.apache.camel.avro.generated.KeyValueProtocol"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result-in-message-name"
argument_list|)
expr_stmt|;
comment|//In Only with existing interface
name|from
argument_list|(
literal|"direct:in-reflection"
argument_list|)
operator|.
name|to
argument_list|(
literal|"avro:http:localhost:"
operator|+
name|avroPortReflection
operator|+
literal|"/setName?protocolClassName=org.apache.camel.avro.test.TestReflection&singleParameter=true"
argument_list|)
expr_stmt|;
comment|//InOut
name|from
argument_list|(
literal|"direct:inout"
argument_list|)
operator|.
name|to
argument_list|(
literal|"avro:http:localhost:"
operator|+
name|avroPort
operator|+
literal|"?protocolClassName=org.apache.camel.avro.generated.KeyValueProtocol"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result-inout"
argument_list|)
expr_stmt|;
comment|//InOut with message in route
name|from
argument_list|(
literal|"direct:inout-message-name"
argument_list|)
operator|.
name|to
argument_list|(
literal|"avro:http:localhost:"
operator|+
name|avroPort
operator|+
literal|"/get?protocolClassName=org.apache.camel.avro.generated.KeyValueProtocol"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result-inout-message-name"
argument_list|)
expr_stmt|;
comment|//InOut with existing interface
name|from
argument_list|(
literal|"direct:inout-reflection"
argument_list|)
operator|.
name|to
argument_list|(
literal|"avro:http:localhost:"
operator|+
name|avroPortReflection
operator|+
literal|"/increaseAge?protocolClassName=org.apache.camel.avro.test.TestReflection&singleParameter=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result-inout-reflection"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

