begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dropbox
operator|.
name|util
package|;
end_package

begin_enum
DECL|enum|DropboxResultHeader
specifier|public
enum|enum
name|DropboxResultHeader
block|{
DECL|enumConstant|DOWNLOADED_FILE
DECL|enumConstant|DOWNLOADED_FILES
DECL|enumConstant|UPLOADED_FILE
DECL|enumConstant|UPLOADED_FILES
DECL|enumConstant|FOUND_FILES
DECL|enumConstant|DELETED_PATH
DECL|enumConstant|MOVED_PATH
name|DOWNLOADED_FILE
block|,
name|DOWNLOADED_FILES
block|,
name|UPLOADED_FILE
block|,
name|UPLOADED_FILES
block|,
name|FOUND_FILES
block|,
name|DELETED_PATH
block|,
name|MOVED_PATH
block|}
end_enum

end_unit

