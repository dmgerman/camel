begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb.gridfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
operator|.
name|gridfs
package|;
end_package

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|ReadPreference
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|WriteConcern
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
name|Converter
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
name|Exchange
import|;
end_import

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|GridFsConverter
specifier|public
specifier|final
class|class
name|GridFsConverter
block|{
DECL|method|GridFsConverter ()
specifier|private
name|GridFsConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toWriteConcern (String value, Exchange exchange)
specifier|public
specifier|static
name|WriteConcern
name|toWriteConcern
parameter_list|(
name|String
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|WriteConcern
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toReadPreference (String value, Exchange exchange)
specifier|public
specifier|static
name|ReadPreference
name|toReadPreference
parameter_list|(
name|String
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|ReadPreference
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

