// This class defines values associated with a building.
public class BuildingDetails {
    int buildingNum;
    int executed_time;
    int total_time;

    public BuildingDetails()
    {
        this.buildingNum = 0;
        this.executed_time = 0;
        this.total_time = 0;
    }

    public BuildingDetails(int bNum, int Etime, int tTotal)
    {
        this.buildingNum = bNum;
        this.executed_time = Etime;
        this.total_time = tTotal;
    }
}
