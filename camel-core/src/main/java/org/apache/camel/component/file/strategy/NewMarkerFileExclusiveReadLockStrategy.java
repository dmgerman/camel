begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.file.strategy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|strategy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|component
operator|.
name|file
operator|.
name|GenericFileExclusiveReadLockStrategy
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
name|component
operator|.
name|file
operator|.
name|GenericFileOperations
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
name|component
operator|.
name|file
operator|.
name|GenericFile
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|NewMarkerFileExclusiveReadLockStrategy
specifier|public
class|class
name|NewMarkerFileExclusiveReadLockStrategy
implements|implements
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|File
argument_list|>
block|{
DECL|method|acquireExclusiveReadLock (GenericFileOperations<File> fileGenericFileOperations, GenericFile<File> fileGenericFile)
specifier|public
name|boolean
name|acquireExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|fileGenericFileOperations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|fileGenericFile
parameter_list|)
block|{
comment|// create the .camelFile
return|return
literal|false
return|;
block|}
DECL|method|releaseExclusiveReadLock (GenericFileOperations<File> fileGenericFileOperations, GenericFile<File> fileGenericFile)
specifier|public
name|void
name|releaseExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|fileGenericFileOperations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|fileGenericFile
parameter_list|)
block|{
comment|// delete the .camelFile
block|}
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

