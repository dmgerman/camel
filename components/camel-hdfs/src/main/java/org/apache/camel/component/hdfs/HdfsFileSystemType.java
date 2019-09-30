begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
package|;
end_package

begin_enum
DECL|enum|HdfsFileSystemType
specifier|public
enum|enum
name|HdfsFileSystemType
block|{
DECL|enumConstant|LOCAL
name|LOCAL
block|{
annotation|@
name|Override
specifier|public
name|StringBuilder
name|getHdfsPath
parameter_list|(
name|HdfsConfiguration
name|config
parameter_list|)
block|{
name|StringBuilder
name|hpath
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|hpath
operator|.
name|append
argument_list|(
literal|"file://"
argument_list|)
expr_stmt|;
name|hpath
operator|.
name|append
argument_list|(
name|config
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|hasSplitStrategies
argument_list|()
condition|)
block|{
name|hpath
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
return|return
name|hpath
return|;
block|}
block|}
block|,
DECL|enumConstant|HDFS
name|HDFS
block|{
annotation|@
name|Override
specifier|public
name|StringBuilder
name|getHdfsPath
parameter_list|(
name|HdfsConfiguration
name|config
parameter_list|)
block|{
name|StringBuilder
name|hpath
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|hpath
operator|.
name|append
argument_list|(
literal|"hdfs://"
argument_list|)
expr_stmt|;
name|hpath
operator|.
name|append
argument_list|(
name|config
operator|.
name|getHostName
argument_list|()
argument_list|)
expr_stmt|;
name|hpath
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|hpath
operator|.
name|append
argument_list|(
name|config
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|hpath
operator|.
name|append
argument_list|(
name|config
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|hasSplitStrategies
argument_list|()
condition|)
block|{
name|hpath
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
return|return
name|hpath
return|;
block|}
block|}
block|;
DECL|method|getHdfsPath (HdfsConfiguration conf)
specifier|public
specifier|abstract
name|StringBuilder
name|getHdfsPath
parameter_list|(
name|HdfsConfiguration
name|conf
parameter_list|)
function_decl|;
block|}
end_enum

end_unit

