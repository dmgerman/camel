begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*   Licensed to the Apache Software Foundation (ASF) under one or more   contributor license agreements.  See the NOTICE file distributed with   this work for additional information regarding copyright ownership.   The ASF licenses this file to You under the Apache License, Version 2.0   (the "License"); you may not use this file except in compliance with   the License.  You may obtain a copy of the License at         http://www.apache.org/licenses/LICENSE-2.0    Unless required by applicable law or agreed to in writing, software   distributed under the License is distributed on an "AS IS" BASIS,   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   See the License for the specific language governing permissions and   limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox.dto
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
name|dto
package|;
end_package

begin_class
DECL|class|DropboxMoveResult
specifier|public
class|class
name|DropboxMoveResult
block|{
DECL|field|oldPath
specifier|private
specifier|final
name|String
name|oldPath
decl_stmt|;
DECL|field|newPath
specifier|private
specifier|final
name|String
name|newPath
decl_stmt|;
DECL|method|DropboxMoveResult (String oldPath, String newPath)
specifier|public
name|DropboxMoveResult
parameter_list|(
name|String
name|oldPath
parameter_list|,
name|String
name|newPath
parameter_list|)
block|{
name|this
operator|.
name|oldPath
operator|=
name|oldPath
expr_stmt|;
name|this
operator|.
name|newPath
operator|=
name|newPath
expr_stmt|;
block|}
DECL|method|getOldPath ()
specifier|public
name|String
name|getOldPath
parameter_list|()
block|{
return|return
name|oldPath
return|;
block|}
DECL|method|getNewPath ()
specifier|public
name|String
name|getNewPath
parameter_list|()
block|{
return|return
name|newPath
return|;
block|}
comment|/*       Object payload contained in Exchange       Exchange Header and Body contains the mode path       @param exchange      */
comment|//    @Override
comment|//    public void populateExchange(Exchange exchange) {
comment|//        String movedPath = (String)resultEntries;
comment|//        exchange.getIn().setHeader(DropboxResultHeader.MOVED_PATH.name(), movedPath);
comment|//        exchange.getIn().setBody(movedPath);
comment|//    }
block|}
end_class

end_unit

