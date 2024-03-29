begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|// CHECKSTYLE:OFF
end_comment

begin_comment
comment|/**  * Autogenerated by Avro  *   * DO NOT EDIT DIRECTLY  */
end_comment

begin_package
DECL|package|org.apache.camel.avro.generated
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
package|;
end_package

begin_interface
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
DECL|interface|KeyValueProtocol
specifier|public
interface|interface
name|KeyValueProtocol
block|{
DECL|field|PROTOCOL
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Protocol
name|PROTOCOL
init|=
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Protocol
operator|.
name|parse
argument_list|(
literal|"{\"protocol\":\"KeyValueProtocol\",\"namespace\":\"org.apache.camel.avro.generated\",\"types\":[{\"type\":\"record\",\"name\":\"Key\",\"fields\":[{\"name\":\"key\",\"type\":\"string\"}]},{\"type\":\"record\",\"name\":\"Value\",\"fields\":[{\"name\":\"value\",\"type\":\"string\"}]}],\"messages\":{\"put\":{\"request\":[{\"name\":\"key\",\"type\":\"Key\"},{\"name\":\"value\",\"type\":\"Value\"}],\"response\":\"null\"},\"get\":{\"request\":[{\"name\":\"key\",\"type\":\"Key\"}],\"response\":\"Value\"}}}"
argument_list|)
decl_stmt|;
DECL|method|put (org.apache.camel.avro.generated.Key key, org.apache.camel.avro.generated.Value value)
name|java
operator|.
name|lang
operator|.
name|Void
name|put
parameter_list|(
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
name|Key
name|key
parameter_list|,
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
name|Value
name|value
parameter_list|)
throws|throws
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|AvroRemoteException
function_decl|;
DECL|method|get (org.apache.camel.avro.generated.Key key)
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
name|Value
name|get
parameter_list|(
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
name|Key
name|key
parameter_list|)
throws|throws
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|AvroRemoteException
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
DECL|interface|Callback
specifier|public
interface|interface
name|Callback
extends|extends
name|KeyValueProtocol
block|{
DECL|field|PROTOCOL
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Protocol
name|PROTOCOL
init|=
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
operator|.
name|PROTOCOL
decl_stmt|;
DECL|method|put (org.apache.camel.avro.generated.Key key, org.apache.camel.avro.generated.Value value, org.apache.avro.ipc.Callback<java.lang.Void> callback)
name|void
name|put
parameter_list|(
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
name|Key
name|key
parameter_list|,
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
name|Value
name|value
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|Callback
argument_list|<
name|java
operator|.
name|lang
operator|.
name|Void
argument_list|>
name|callback
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|IOException
function_decl|;
DECL|method|get (org.apache.camel.avro.generated.Key key, org.apache.avro.ipc.Callback<org.apache.camel.avro.generated.Value> callback)
name|void
name|get
parameter_list|(
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
name|Key
name|key
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|Callback
argument_list|<
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
name|Value
argument_list|>
name|callback
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|IOException
function_decl|;
block|}
block|}
end_interface

end_unit

