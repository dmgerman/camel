begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.kinesis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|kinesis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kinesis
operator|.
name|model
operator|.
name|Record
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|RecordStringConverterTest
specifier|public
class|class
name|RecordStringConverterTest
block|{
annotation|@
name|Test
DECL|method|convertRecordToString ()
specifier|public
name|void
name|convertRecordToString
parameter_list|()
throws|throws
name|Exception
block|{
name|Record
name|record
init|=
operator|new
name|Record
argument_list|()
operator|.
name|withSequenceNumber
argument_list|(
literal|"1"
argument_list|)
operator|.
name|withData
argument_list|(
name|ByteBuffer
operator|.
name|wrap
argument_list|(
literal|"this is a String"
operator|.
name|getBytes
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|result
init|=
name|RecordStringConverter
operator|.
name|toString
argument_list|(
name|record
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|is
argument_list|(
literal|"this is a String"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

