begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.fixed.headerfooter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|fixed
operator|.
name|headerfooter
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|DataField
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|FixedLengthRecord
import|;
end_import

begin_class
annotation|@
name|FixedLengthRecord
DECL|class|OrderFooter
specifier|public
class|class
name|OrderFooter
block|{
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|1
argument_list|,
name|length
operator|=
literal|1
argument_list|)
DECL|field|recordType
specifier|private
name|int
name|recordType
init|=
literal|9
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|2
argument_list|,
name|length
operator|=
literal|9
argument_list|,
name|align
operator|=
literal|"R"
argument_list|,
name|paddingChar
operator|=
literal|'0'
argument_list|)
DECL|field|numberOfRecordsInTheFile
specifier|private
name|int
name|numberOfRecordsInTheFile
decl_stmt|;
DECL|method|getRecordType ()
specifier|public
name|int
name|getRecordType
parameter_list|()
block|{
return|return
name|recordType
return|;
block|}
DECL|method|setRecordType (int recordType)
specifier|public
name|void
name|setRecordType
parameter_list|(
name|int
name|recordType
parameter_list|)
block|{
name|this
operator|.
name|recordType
operator|=
name|recordType
expr_stmt|;
block|}
DECL|method|getNumberOfRecordsInTheFile ()
specifier|public
name|int
name|getNumberOfRecordsInTheFile
parameter_list|()
block|{
return|return
name|numberOfRecordsInTheFile
return|;
block|}
DECL|method|setNumberOfRecordsInTheFile (int numberOfRecordsInTheFile)
specifier|public
name|void
name|setNumberOfRecordsInTheFile
parameter_list|(
name|int
name|numberOfRecordsInTheFile
parameter_list|)
block|{
name|this
operator|.
name|numberOfRecordsInTheFile
operator|=
name|numberOfRecordsInTheFile
expr_stmt|;
block|}
block|}
end_class

end_unit

